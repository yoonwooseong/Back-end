package service;

import java.util.List;

import vo.AirdndHomePictureVO;
import vo.AirdndSearchVO;

public interface AirdndSearchServiceI {
	
	List<AirdndSearchVO> searchselect();
	
	List<AirdndHomePictureVO> pictureselect(int home_idx);
	
}