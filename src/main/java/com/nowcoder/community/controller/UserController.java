package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Value("${community.path.upload}")
	private String uploadPath;

	@Value("${community.path.domain}")
	private String domain;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Autowired
	private UserService userService;

	@Autowired
	private HostHolder hostHolder;

	@LoginRequired
	@RequestMapping(path = "/setting", method = RequestMethod.GET)
	public String getSettingPage() {
		return "/site/setting";
	}

	@LoginRequired
	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public String uploadHeader(MultipartFile headerImage, Model model) {
		if (headerImage == null) {
			model.addAttribute("error", "您还没有选择图片！");
			return "/site/setting";
		}

		String fileName = headerImage.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		if (StringUtils.isBlank(suffix)) {
			model.addAttribute("error", "文件的格式不正確！");
			return "/site/setting";
		}

		//生成隨機文件名
		fileName = CommunityUtil.generateUUID() + suffix;
		//確定文件存放的路徑
		File dest = new File(uploadPath + "/" + fileName);
		try {
			headerImage.transferTo(dest);
		} catch (IOException e) {
			logger.error("上傳文件失敗" + e.getMessage());
			throw new RuntimeException("上傳文件失敗，服務器發生異常！", e);
		}

		//更新當前用戶的頭像路徑(web訪問路徑)
		//http://localhost:8080/community/**/*.png
		User user = hostHolder.getUser();
		String headerUrl = domain + contextPath + "/user/header/" + fileName;
		userService.updateHeader(user.getId(), headerUrl);

		return "redirect:/index";
	}

	@RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
	public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
		//服務器存放路徑
		fileName = uploadPath + "/" + fileName;
		//文件的後綴
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		//響應圖片
		response.setContentType("image/" + suffix);
		try (
				FileInputStream fis = new FileInputStream(fileName);
				OutputStream os = response.getOutputStream()
		) {
			byte[] buffer = new byte[1024];
			int b = 0;
			while ((b = fis.read(buffer)) != -1) {
				os.write(buffer, 0, b);
			}
		} catch (IOException e) {
			logger.error("讀取頭像失敗：" + e.getMessage());
		}

	}

	@LoginRequired
	@RequestMapping(path = "/changePassword", method = RequestMethod.POST)
	public String changePassword(String oldPassword, String newPassword, String newPasswordAgain, Model model, HttpServletRequest request) {
		Map<String, Object> map = userService.changePassword(oldPassword, newPassword, newPasswordAgain, request);
		if (!map.isEmpty()) {
			model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
			model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
			return "/site/setting";
		}
		model.addAttribute("msg", "修改密码成功，请重新登录！");
		model.addAttribute("target", "/login");
		return "/site/operate-result";
	}
}
