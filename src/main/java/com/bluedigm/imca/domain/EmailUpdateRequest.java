package com.bluedigm.imca.domain;

import lombok.Data;

@Data
public class EmailUpdateRequest {

	private EmailSetting emailSetting;

	private Cpe cpe;

//	public EmailSetting getEmailSetting() {
//		return emailSetting;
//	}
//
//	public void setEmailSetting(EmailSetting emailSetting) {
//		this.emailSetting = emailSetting;
//	}
//
//	public Cpe getCpe() {
//		return cpe;
//	}
//
//	public void setCpe(Cpe cpe) {
//		this.cpe = cpe;
//	}
//
//	@Override
//	public String toString() {
//		return String.format("EmailUpdateRequest [emailSetting=%s, cpe=%s]", emailSetting, cpe);
//	}

}
