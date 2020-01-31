package com.bluedigm.imca;

public class EmailSetting {

	private String email;

	private String smtp;

	private String sendEmail;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((sendEmail == null) ? 0 : sendEmail.hashCode());
		result = prime * result + ((smtp == null) ? 0 : smtp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailSetting other = (EmailSetting) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (sendEmail == null) {
			if (other.sendEmail != null)
				return false;
		} else if (!sendEmail.equals(other.sendEmail))
			return false;
		if (smtp == null) {
			if (other.smtp != null)
				return false;
		} else if (!smtp.equals(other.smtp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("EmailSetting [email=%s, smtp=%s, sendEmail=%s]", email, smtp, sendEmail);
	}

}
