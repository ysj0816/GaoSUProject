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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCheckUnitId() {
		return checkUnitId;
	}

	public void setCheckUnitId(String checkUnitId) {
		this.checkUnitId = checkUnitId;
	}

	public String getCheckProjectId() {
		return checkProjectId;
	}

	public void setCheckProjectId(String checkProjectId) {
		this.checkProjectId = checkProjectId;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}

	@Id(autoincrement = true)
	Long id;
	private String checkUnitId;

	@Override
	public String toString() {
		return "DailyCheck{" +
				"id=" + id +
				", checkUnitId='" + checkUnitId + '\'' +
				", checkProjectId='" + checkProjectId + '\'' +
				", userid='" + userid + '\'' +
				", checkresult='" + checkresult + '\'' +
				", content='" + content + '\'' +
				", deductpoint='" + deductpoint + '\'' +
				", checkid='" + checkid + '\'' +
				", path=" + path +
				'}';
	}

	private String checkProjectId;
	private String userid;
	private String checkresult;
	private String content;
	private String deductpoint;
	private String checkid;
	@Convert(columnType = String.class, converter = StringConverter.class)
	private List<String> path;

	@Generated(hash = 36573168)
	public DailyCheck(Long id, String checkUnitId, String checkProjectId,
									String userid, String checkresult, String content, String deductpoint,
									String checkid, List<String> path) {
					this.id = id;
					this.checkUnitId = checkUnitId;
					this.checkProjectId = checkProjectId;
					this.userid = userid;
					this.checkresult = checkresult;
					this.content = content;
					this.deductpoint = deductpoint;
					this.checkid = checkid;
					this.path = path;
	}

	@Generated(hash = 234058891)
	public DailyCheck() {
	}


}
