package kr.accom.vo;

import kr.member.vo.MemberVO;

public class AccomInfoVO {
	private int info_num;
	private int accom_num;
	private int mem_num;
	private int accom_yn;
	
	private AccomVO accomVO;
	private MemberVO memberVO;
	
	public int getInfo_num() {
		return info_num;
	}
	public void setInfo_num(int info_num) {
		this.info_num = info_num;
	}
	public int getAccom_num() {
		return accom_num;
	}
	public void setAccom_num(int accom_num) {
		this.accom_num = accom_num;
	}
	public int getMem_num() {
		return mem_num;
	}
	public void setMem_num(int mem_num) {
		this.mem_num = mem_num;
	}
	public int getAccom_yn() {
		return accom_yn;
	}
	public void setAccom_yn(int accom_yn) {
		this.accom_yn = accom_yn;
	}
	public AccomVO getAccomVO() {
		return accomVO;
	}
	public void setAccomVO(AccomVO accomVO) {
		this.accomVO = accomVO;
	}
	public MemberVO getMemberVO() {
		return memberVO;
	}
	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}	
}
