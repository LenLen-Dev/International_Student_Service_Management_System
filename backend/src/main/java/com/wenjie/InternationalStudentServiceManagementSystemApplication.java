/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie
 * @className: InternationalStudentServiceManagementSystemApplication
 * @description: 留学生服务与管理系统后端启动入口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wenjie.modules.*.mapper")
@EnableScheduling
public class InternationalStudentServiceManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternationalStudentServiceManagementSystemApplication.class, args);
    }
}
