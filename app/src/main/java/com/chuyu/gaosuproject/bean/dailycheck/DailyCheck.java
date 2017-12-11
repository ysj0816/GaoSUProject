package com.chuyu.gaosuproject.bean.dailycheck;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.File;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/12/4.
 */
@Entity
public class DailyCheck {

	@Id(autoincrement = true)
	Long id;
	private String userid;
	private String checkunit;
	private String checkproject;
	private String checkresult;
	private String content;
	private String deductpoint;
	private String checkid;
	@Convert(columnType = String.class, converter = StringConverter.class)
	private List<String> images;

	@Generated(hash = 1779440453)
	public DailyCheck(Long id, String userid, String checkunit, String checkproject,
									String checkresult, String content, String deductpoint, String checkid,
									List<String> images) {
					this.id = id;
					this.userid = userid;
					this.checkunit = checkunit;
					this.checkproject = checkproject;
					this.checkresult = checkresult;
					this.content = content;
					this.deductpoint = deductpoint;
					this.checkid = checkid;
					this.images = images;
	}

	public DailyCheck( String userid, String checkunit, String checkproject,
					  String checkresult, String content, String deductpoint, String checkid,
					  List<String> images) {
		this.userid = userid;
		this.checkunit = checkunit;
		this.checkproject = checkproject;
		this.checkresult = checkresult;
		this.content = content;
		this.deductpoint = deductpoint;
		this.checkid = checkid;
		this.images = images;
	}

	@Generated(hash = 234058891)
	public DailyCheck() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCheckunit() {
		return checkunit;
	}

	public void setCheckunit(String checkunit) {
		this.checkunit = checkunit;
	}

	public String getCheckproject() {
		return checkproject;
	}

	public void setCheckproject(String checkproject) {
		this.checkproject = checkproject;
	}

	public String getCheckresult() {
		return checkresult;
	}

	public void setCheckresult(String checkresult) {
		this.checkresult = checkresult;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDeductpoint() {
		return deductpoint;
	}

	public void setDeductpoint(String deductpoint) {
		this.deductpoint = deductpoint;
	}

	public String getCheckid() {
		return checkid;
	}

	public void setCheckid(String checkid) {
		this.checkid = checkid;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
}
