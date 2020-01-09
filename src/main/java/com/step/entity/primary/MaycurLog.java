package com.step.entity.primary;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/***
 * 用于记录每刻报销操作日志
 */
@Entity
@Table(name="z_maycur_log")
@Data
public class MaycurLog {
    public enum LogType{
        FnaBudgetExec("预算执行");
        private String text;

        LogType(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    /***
     * 请求ID
     */
    @Id
    @Column(name="request_id")
    private String requestId;
    /***
     * 日志类型
     */
    @Column(name="log_type")
    @Enumerated(EnumType.STRING)
    private LogType logType;
    /***
     * 内容
     */
    @Column(name="content")
    private String content;
    /***
     * 修改时间
     */
    @Column(name="last_modify_time")
    private Date lastModifyTime;
    /***
     * 是否处理0未成功 ，1已处理
     */
    @Column(name="status")
    private String status;

}
