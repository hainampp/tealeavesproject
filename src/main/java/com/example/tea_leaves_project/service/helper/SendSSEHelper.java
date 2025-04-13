package com.example.tea_leaves_project.Service.helper;

import com.example.tea_leaves_project.Payload.Request.ScanEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SendSSEHelper {
    private final List<ScanEmitter> emitters = new CopyOnWriteArrayList<>();

    // thêm emiiter của (user)

    public  void addEmitter(long userid, SseEmitter emitter) {
        log.info("add emitter cho user : {}", userid);
        ScanEmitter scanEmitter = new ScanEmitter(userid,emitter);
        emitters.add(scanEmitter);
        emitter.onCompletion(() -> emitters.remove(scanEmitter));
        emitter.onTimeout(() -> emitters.remove(scanEmitter));
    }

    // tìm kiếm emitters của user
    public void notifyQrCodeScanned(Long userid,String username) {
        for (ScanEmitter scanEmitter : emitters) {
            if (scanEmitter.getUserid()==userid) {
                try {
                    // Lấy SecurityContext hiện tại
                    SecurityContext securityContext = SecurityContextHolder.getContext();

                    // Đặt SecurityContext cho thread hiện tại
                    SecurityContextHolder.setContext(scanEmitter.getSecurityContext());

                    log.info("[SendSSEHelper - notifyQrCodeScanned] Send notice Scan {}]",username);
                    scanEmitter.getEmitter().send(SseEmitter.event().name("scan").data( username + " đã được quét"));

                    // khôi phục luồng
                    SecurityContextHolder.setContext(securityContext);
                } catch (IOException e) {
                    emitters.remove(scanEmitter);
                }
            }
        }
    }
    public void notifyWeigh(Long userid,String username,double weight) {
        for (ScanEmitter scanEmitter : emitters) {
            if (scanEmitter.getUserid()==userid) {
                try {
                    log.info("[SendSSEHelper - notifyWeigh] Send notice Weigh to {} with {}",username,weight);
                    scanEmitter.getEmitter().send(SseEmitter.event().name("weigh").data(username+"vừa thực hiện cân bao chè với khối lượng : " + weight));
                }catch (IOException e) {
                    emitters.remove(scanEmitter);
                }
            }
        }
    }
    // Trong SendSSEHelper
    public void sendHeartbeatToAll() {
        List<ScanEmitter> deadEmitters = new ArrayList<>();

        for (ScanEmitter emitter : emitters) {
            log.info("Kiem tra emitter cho user : {}", emitter.getUserid());
            try {
                emitter.getEmitter().send(SseEmitter.event()
                        .name("heartbeat")
                        .data("ping"));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }

        // Xóa các emitter lỗi
        log.info("Xóa các emiiters không còn được connect");
        emitters.removeAll(deadEmitters);
    }
}
