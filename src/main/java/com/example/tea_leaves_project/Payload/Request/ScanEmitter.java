package com.example.tea_leaves_project.Payload.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
@AllArgsConstructor
@Data
public class ScanEmitter {

        private final SseEmitter emitter;
        private final long userid;
        private SecurityContext securityContext;
        public ScanEmitter(Long userid, SseEmitter emitter) {
                this.userid = userid;
                this.emitter = emitter;
                // Lưu context hiện tại khi tạo emitter
                this.securityContext = SecurityContextHolder.getContext();
        }

        // constructor, getter, ...
}
