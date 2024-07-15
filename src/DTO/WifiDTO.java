package DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class WifiDTO {
    private double distance;        	//거리
    private String xSwifiMgrNo;     	//관리번호
    private String xSwifiWrdofc;     	//자치구
    private String xSwifiMainNm;    	//와이파이명
    private String xSwifiAdres1;    	//도로명 주소
    private String xSwifiAdres2;    	//상세 주소
    private String xSwifiInstlFloor;    //설치위치(층)
    private String xSwifiInstlTy;   	//설치 유형
    private String xSwifiInstlMby;  	//설치 기관
    private String xSwifiSvcSe;     	//서비스 구분
    private String xSwifiCmcwr;     	//망 종류
    private String xSwifiCnstcYear; 	//설치년도
    private String xSwifiInoutDoor;  	// 실내외구분
    private String xSwifiRemars3;     	// wifi 접속환경
    private String lat;                 // x좌표
    private String lnt;                 // y좌표
    private String workDttm;    		// 작업일자
	
}
