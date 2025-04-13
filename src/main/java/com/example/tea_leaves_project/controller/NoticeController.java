package com.example.tea_leaves_project.Controller;

import com.example.tea_leaves_project.Exception.ApiException;
import com.example.tea_leaves_project.Model.entity.Users;
import com.example.tea_leaves_project.Responsitory.UserRepository;
import com.example.tea_leaves_project.Service.helper.SendSSEHelper;
import com.example.tea_leaves_project.Util.JwtUtilHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    JwtUtilHelper jwtUtil;
    @Autowired
    SendSSEHelper sendSSEHelper;
    @Autowired
    UserRepository userRepository;
    private String getTokenFromHeader(WebRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(WebRequest request) {
        // Trước tiên lấy userId trong một context transaction riêng biệt
        Long userId = getUserIdFromToken(request);

        // Sau đó mới tạo emitter (khi này kết nối DB đã đóng)
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sendSSEHelper.addEmitter(userId, emitter);

        return emitter;
    }

    // Phương thức riêng biệt với transaction rõ ràng
    @Transactional(readOnly = true)
    public Long getUserIdFromToken(WebRequest request) {
        String token = getTokenFromHeader(request);

        if (token == null || token.isEmpty()) {
            throw ApiException.ErrUnauthorized().build();
        }

        if(!jwtUtil.verifyToken(token)) {
            throw ApiException.ErrBadCredentials().build();
        }

        String email = jwtUtil.getEmail(token);
        Users user = userRepository.findUserByEmail(email);

        return user.getUserid();
    }
    @Scheduled(fixedRate = 30000) // Gửi mỗi 30 giây
    public void sendHeartbeat() {
        sendSSEHelper.sendHeartbeatToAll();
    }
}
