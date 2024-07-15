package DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {

	private int id; 		// id
	private String lat; 	// 위도
	private String lnt; 	// 경도
	private String date; 	// 날짜
}
