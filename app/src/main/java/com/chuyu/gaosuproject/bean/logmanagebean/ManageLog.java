package com.chuyu.gaosuproject.bean.logmanagebean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2017/11/30.
 */
@Entity
public class ManageLog {
	@Id(autoincrement = true)
	Long id;




	private String authoruserid;
	//时间
	private String createtime;
	private String finishwork;

	@Override
	public String toString() {
		return "ManageLog{" +
				"id=" + id +
				", authoruserid='" + authoruserid + '\'' +
				", createtime='" + createtime + '\'' +
				", finishwork='" + finishwork + '\'' +
				", unfinishwork='" + unfinishwork + '\'' +
				", needassistwork='" + needassistwork + '\'' +
				", remark='" + remark + '\'' +
				", category='" + category + '\'' +
				'}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthoruserid() {
		return authoruserid;
	}

	public void setAuthoruserid(String authoruserid) {
		this.authoruserid = authoruserid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getFinishwork() {
		return finishwork;
	}

	public void setFinishwork(String finishwork) {
		this.finishwork = finishwork;
	}

	public String getUnfinishwork() {
		return unfinishwork;
	}

	public void setUnfinishwork(String unfinishwork) {
		this.unfinishwork = unfinishwork;
	}

	public String getNeedassistwork() {
		return needassistwork;
	}

	public void setNeedassistwork(String needassistwork) {
		this.needassistwork = needassistwork;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private String unfinishwork;
	private String needassistwork;
	private String remark;
	private String category;

	@Generated(hash = 253943459)
	public ManageLog(Long id, String authoruserid, String createtime,
									String finishwork, String unfinishwork, String needassistwork,
									String remark, String category) {
					this.id = id;
					this.authoruserid = authoruserid;
					this.createtime = createtime;
					this.finishwork = finishwork;
					this.unfinishwork = unfinishwork;
					this.needassistwork = needassistwork;
					this.remark = remark;
					this.category = category;
	}

	@Generated(hash = 1059474224)
	public ManageLog() {
	}




}
