package com.airdnd.back;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;

import service.AirdndSearchService;
import vo.AirdndHomePictureVO;
import vo.AirdndSearchVO;

@Controller
public class SearchController {

	@Autowired
	AirdndSearchService airdndsearchService;

	@RequestMapping(value="/search/user/{userId}/location/{location}/checkIn/{checkIn}/checkOut/{checkOut}/guests/{guests}/lat_from/{lat_from}"
			+ "/lng_from/{lng_from}/lat_to/{lat_to}/lng_to/{lng_to}/filter_refund/{filter_refund}/filter_roomType_house/{filter_roomType_house}/"
			+ "filter_roomType_private/{filter_roomType_private}/filter_roomType_shared/{filter_roomType_shared}/filter_price_min/{filter_price_min}/"
			+ "filter_price_max/{filter_price_max}/filter_instantBooking/{filter_instantBooking}/filter_bedroom_bed/{filter_bedroom_bed}"
			+ "/filter_bedroom_room/{filter_bedroom_room}/filter_bedroom_bathroom/{filter_bedroom_bathroom}/filter_convenience/{filter_convenience}/"
			+ "filter_convenienceList/{filter_convenienceList}/filter_facilityList/{filter_facilityList}/filter_hostLangList/{filter_hostLangList}/page/{page}",
			method=RequestMethod.GET, produces = "application/json;charset=utf8", consumes = MediaType.ALL_VALUE)
	@ResponseBody         // 어디검색, 몇박며칠, 인원수...
	public String check(HttpServletRequest request, HttpServletResponse response, @PathVariable int userId, @PathVariable String location, @PathVariable String checkIn, @PathVariable String checkOut,
			@PathVariable int guests, @PathVariable double lat_from, @PathVariable double lng_from, @PathVariable double lat_to, @PathVariable double lng_to,
			@PathVariable boolean filter_refund, @PathVariable boolean filter_roomType_house, @PathVariable boolean filter_roomType_private,
			@PathVariable boolean filter_roomType_shared, @PathVariable int filter_price_min, @PathVariable int filter_price_max,
			@PathVariable boolean filter_instantBooking, @PathVariable int filter_bedroom_bed, @PathVariable int filter_bedroom_room,
			@PathVariable int filter_bedroom_bathroom, @PathVariable boolean filter_convenience, @PathVariable String filter_convenienceList,
			@PathVariable String filter_facilityList, @PathVariable String filter_hostLangList, @PathVariable int page) {

		HttpHeaders resHeaders = new HttpHeaders();
		resHeaders.add("Content-Type", "application/json;charset=UTF-8");
		try {
			location = URLDecoder.decode(location, "utf-8");
			checkIn = URLDecoder.decode(checkIn, "utf-8");
			checkOut = URLDecoder.decode(checkOut, "utf-8");

			filter_convenienceList = URLDecoder.decode(filter_convenienceList, "utf-8");
			filter_facilityList = URLDecoder.decode(filter_facilityList, "utf-8");
			filter_hostLangList = URLDecoder.decode(filter_hostLangList, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(checkIn.equals("null")) {
			System.out.println("null : " + checkIn);
		}else if(checkIn.equals("")) {
			System.out.println("없음 : " + checkIn);
		}else if(checkIn.isEmpty()) {
			System.out.println("isempty : " + checkIn);
		}else {
			System.out.println("?? : " + checkIn);
		}
		JSONObject res = new JSONObject();

		//search_list : 페이지별 숙소 리스트------------------
		if(filter_price_max == 0) {
			filter_price_max = 2147483646;
		}
		//if(filter_roomType_house)
		List<AirdndSearchVO> search_list = airdndsearchService.searchselect(location, page, filter_price_min, filter_price_max);
		int size = search_list.size();

		List<JSONObject> homes = new ArrayList<JSONObject>();

		Double addLat = 0.0000000;
		Double addLng = 0.0000000;

		for(int i = 0; i < size; i++) {
			int home_idx = search_list.get(i).getHome_idx();

			List<AirdndHomePictureVO> picture = airdndsearchService.pictureselect(home_idx);
			List<String> picture_arr = new ArrayList<String>();

			JSONObject homes_info = new JSONObject();
			JSONObject latlng = new JSONObject();

			for(int j = 0; j < picture.size(); j++) {
				picture_arr.add(picture.get(j).getUrl());
			}

			search_list.get(i).setUrl(picture_arr);
			String lat = search_list.get(i).getLat();
			String lng = search_list.get(i).getLng();
			latlng.put("lat", lat);
			latlng.put("lng", lng);
			addLat +=  Double.parseDouble(lat);
			addLng +=  Double.parseDouble(lng);

			homes_info.put("homeId", search_list.get(i).getHome_idx());
			homes_info.put("isSuperhost", search_list.get(i).getIsSuperHost());
			homes_info.put("isBookmarked", "아직안받아옴");
			homes_info.put("imageArray", search_list.get(i).getUrl());
			homes_info.put("imageCount", search_list.get(i).getUrl().size());
			homes_info.put("subTitle", search_list.get(i).getSub_title());
			homes_info.put("title", search_list.get(i).getTitle());
			homes_info.put("feature", "최대 인원 " + search_list.get(i).getFilter_max_person() + "명 · 침실 " + search_list.get(i).getFilter_bedroom() + 
					"개 · 침대 " + search_list.get(i).getFilter_bed() + "개 · 욕실 " + search_list.get(i).getFilter_bathroom() + "개");
			homes_info.put("rating", search_list.get(i).getRating());
			homes_info.put("reviewCount", search_list.get(i).getReview_num());
			homes_info.put("price", search_list.get(i).getPrice());
			homes_info.put("location", latlng);

			homes.add(homes_info);

		}
		Double avgLat =(Math.round(addLat/size*10000000)/10000000.0); 
		Double avgLng =(Math.round(addLng/size*10000000)/10000000.0); 
		//Double avgLng = (double) (Math.round((addLng/size)*10000000)/10000000);


		res.put("homes", homes);

		//가격 분포도--------------------------------
		List<AirdndSearchVO> pricelist = airdndsearchService.unitpriceselect(location);
		List<Integer> price_array = new ArrayList();
		int start = 0;
		int end = 2;
		int save_num = 0;
		for(int i = 0; i < 50; i++) {
			for(AirdndSearchVO price : pricelist) {
				if(start <= (int)price.getPrice()/10000 && end > (int)price.getPrice()/10000) {
					save_num++;
				}
			}
			price_array.add(save_num);
			start += 2;
			end += 2;
			save_num = 0;
		}

		res.put("priceArray", price_array);

		//필터조건들 ---------------------------------
		List<AirdndSearchVO> facilities = airdndsearchService.facilityList(location);
		//List<AirdndUserVO> hostlanlists = airdndsearchService.hostLanlist(location);

		List<String> facilityList = new ArrayList<String>();
		List<String> convenienceList = new ArrayList<String>();
		List<String> hostLangList = new ArrayList<String>();

		for(AirdndSearchVO filter : facilities) {
			if( !filter.getFacilityList().contains("이용 불가:") ) {
				String element = filter.getFacilityList();
				if( element.contains("수영장") ||  element.contains("주차") || element.contains("자쿠지") || element.contains("헬스장") ) {
					facilityList.add(element);
				} else {
					convenienceList.add(element);
				}
			}
		}
		hostLangList.add("영어");
		hostLangList.add("프랑스어");
		hostLangList.add("한국어");
		hostLangList.add("스페인어");
		hostLangList.add("중국어");

		//for(AirdndUserVO filter : hostlanlists) {
		//  hostlanguagelist.add(filter.getHostLanlist());
		//}

		JSONObject filterCondition = new JSONObject();
		filterCondition.put("instantBooking", true);
		filterCondition.put("bedroom", true);
		filterCondition.put("convenience", true);
		filterCondition.put("convenienceList", convenienceList);
		filterCondition.put("facilityList", facilityList);
		filterCondition.put("hostLangList", hostLangList);

		//--------최근 본 목록 코드 작성------------ 
		List<JSONObject> recentHomes = new ArrayList<JSONObject>();//이걸 쿠키로 받아와 검색하는 쿼리문
		List<Integer> recentHomesIdx = new ArrayList<Integer>();
		List<AirdndSearchVO> recentHomeOne = new ArrayList<AirdndSearchVO>();

		Cookie[] cookies = request.getCookies();

		if(cookies == null) {
			recentHomes.add(null);
		}else{
			for (Cookie cookie : cookies) {
				if(cookie.getName().contains("AirdndRH")) {
					recentHomesIdx.add(Integer.parseInt(cookie.getName()));



					//int recentHomesIdx[] = {596431, 4010129, 4165392};
					for(int recenthome:recentHomesIdx) {
						recentHomeOne = airdndsearchService.select_one(recenthome);

						List<AirdndHomePictureVO> recentPicture = airdndsearchService.pictureselect(recenthome);

						List<String> recent_picture_arr = new ArrayList<String>();

						for(int j = 0; j < recentPicture.size(); j++) {
							recent_picture_arr.add(recentPicture.get(j).getUrl());
						}

						JSONObject recentHome_info = new JSONObject();
						JSONObject latlng = new JSONObject();

						String lat = recentHomeOne.get(0).getLat();
						String lng = recentHomeOne.get(0).getLng();
						latlng.put("lat", lat);
						latlng.put("lng", lng);
						addLat +=  Double.parseDouble(lat);
						addLng +=  Double.parseDouble(lng);

						recentHomeOne.get(0).setUrl(recent_picture_arr);
						recentHome_info.put("homeId", recentHomeOne.get(0).getHome_idx());
						recentHome_info.put("isSuperhost", recentHomeOne.get(0).getIsSuperHost());
						recentHome_info.put("isBookmarked", "아직안받아옴");
						recentHome_info.put("imageArray", recentHomeOne.get(0).getUrl());
						recentHome_info.put("imageCount", recentHomeOne.get(0).getUrl().size());
						recentHome_info.put("subTitle", recentHomeOne.get(0).getSub_title());
						recentHome_info.put("title", recentHomeOne.get(0).getTitle());
						recentHome_info.put("rating", recentHomeOne.get(0).getRating());
						recentHome_info.put("reviewCount", recentHomeOne.get(0).getReview_num());
						recentHome_info.put("price", recentHomeOne.get(0).getPrice());
						recentHome_info.put("location", latlng);


						System.out.println("inner : " + recentHome_info.get("homeId") + recentHome_info.get("subTitle") + recentHome_info.get("imageArray"));

						recentHomes.add(recentHome_info);

						System.out.println("recenhomes : "  + recentHomes.toString());

					}
					//recentHomeList만 뿌려주면 끝
				}
			}
		}

		JSONObject mapCenter = new JSONObject();
		mapCenter.put("lat", avgLat);
		mapCenter.put("lng", avgLng);

		res.put("filterCondition", filterCondition);
		//전체 숙소 데이터 개수, 1박 평균 가격 -----------------
		List<AirdndSearchVO> total = airdndsearchService.searchtotalselect(location);

		res.put("recentHomes", recentHomes);
		res.put("dataTotal", total.get(0).getData_total());
		res.put("averagePrice", total.get(0).getAverage_price());
		res.put("mapCenter",mapCenter);

		return res.toString();
	}
}
