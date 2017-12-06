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
	private String UserId;
	//时间
	private String CreateTime;

	@Override
	public String toString() {
		return "ManageLog{" +
				"id=" + id +
				", UserId='" + UserId + '\'' +
				", CreateTime='" + CreateTime + '\'' +
				", FinishWork='" + FinishWork + '\'' +
				", UnFinishWork='" + UnFinishWork + '\'' +
				", NeedAssistWork='" + NeedAssistWork + '\'' +
				", Remark='" + Remark + '\'' +
				", Category='" + Category + '\'' +
				'}';
	}

	private String FinishWork;
	private String UnFinishWork;
	private String NeedAssistWork;
	private String Remark;
	private String Category;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getFinishWork() {
		return FinishWork;
	}

	public void setFinishWork(String finishWork) {
		FinishWork = finishWork;
	}

	public String getUnFinishWork() {
		return UnFinishWork;
	}

	public void setUnFinishWork(String unFinishWork) {
		UnFinishWork = unFinishWork;
	}

	public String getNeedAssistWork() {
		return NeedAssistWork;
	}

	public void setNeedAssistWork(String needAssistWork) {
		NeedAssistWork = needAssistWork;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}



	@Generated(hash = 788985006)
	public ManageLog(Long id, String UserId, String CreateTime, String FinishWork,
									String UnFinishWork, String NeedAssistWork, String Remark,
									String Category) {
					this.id = id;
					this.UserId = UserId;
					this.CreateTime = CreateTime;
					this.FinishWork = FinishWork;
					this.UnFinishWork = UnFinishWork;
					this.NeedAssistWork = NeedAssistWork;
					this.Remark = Remark;
					this.Category = Category;
	}

	@Generated(hash = 1059474224)
	public ManageLog() {
	}



}
