package com.airdnd.back;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import common.Common;
import service.AirdndSearchService;
import vo.AirdndHomePictureVO;
import vo.AirdndSearchVO;

@Controller
public class SearchController {

	@Autowired
	AirdndSearchService airdndsearchService;

	@RequestMapping("/search")
	public String check(Model model) {
		List<AirdndSearchVO> list = airdndsearchService.searchselect();
		int size = list.size();

		List<String> home_idx_picture = new ArrayList<String>();
		
		for(int i = 0; i < size; i++) {
			int home_idx = list.get(i).getHome_idx();
			
			//home_idx에 해당하는 사진들 검색후 list에 담기
			List<AirdndHomePictureVO> picture = airdndsearchService.pictureselect(home_idx);
			
			List<String> picture_arr = new ArrayList<String>();

			for(int j = 0; j < picture.size(); j++) {
				
				picture_arr.add(picture.get(j).getUrl());
			}
			//이게 jsp model로 보내려해서 tostring해서 문자열로 보내는데 프론트엔드로 보낼땐 그냥 보내도 될듯,,,배열로 바로
			home_idx_picture.add(i, picture_arr.toString());
			
		}
		model.addAttribute("list", list);
		model.addAttribute("arr", home_idx_picture);

		return Common.VIEW_PATH + "search.jsp";
	}
}