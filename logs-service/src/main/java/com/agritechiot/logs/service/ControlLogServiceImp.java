package com.agritechiot.logs.service;


import com.agritechiot.logs.model.ControlLog;
import com.agritechiot.logs.repository.ControlLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ControlLogServiceImp implements ControlLogService {
    private final ControlLogRepo controlLogRepo;

    @Override
    public Flux<ControlLog> getPaginatedControlLogs(int page, int size) {
        return null;
        // return controlLogRepo.findAllByPage(page, size);
    }

    @Override
    public Flux<ControlLog> getControlLogs() {
        return controlLogRepo.findAll();
    }

    @Override
    public Mono<ControlLog> saveControlLogs(ControlLog req) {
        ControlLog controlLog = new ControlLog();
        controlLog.setDateTime(req.getDateTime());
        controlLog.setDuration(req.getDuration());
        controlLog.setStatus(req.getStatus());
        controlLog.setSentBy(req.getSentBy());
        controlLog.setDeviceId(req.getDeviceId());
        return controlLogRepo.save(controlLog);
    }

    @Override
    public Mono<ControlLog> updateControlLogs(Integer id, ControlLog req) {
        return controlLogRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("CONTROL_LOG_NOT_FOUND")))
                .map(controlLog -> {
                    controlLog.setId(req.getId());
                    controlLog.setDateTime(req.getDateTime());
                    controlLog.setDuration(req.getDuration());
                    controlLog.setDeviceId(req.getDeviceId());
                    controlLog.setStatus(req.getStatus());
                    controlLog.setSentBy(req.getSentBy());
                    return controlLog;
                }).flatMap(controlLogRepo::save);
    }

    @Override
    public Flux<ControlLog> getControlLogsByDeviceId(String id) {
       return controlLogRepo.findByDeviceId(id);
    }

    @Override
    public Mono<ControlLog> getControlLog(Integer id) {
        return null;
//        return controlLogRepo.findById(id)
//                .switchIfEmpty(Mono.error(new Exception("CONTROL_LOG_NOT_FOUND")));
    }

    @Override
    public Flux<ControlLog> getControlLogsByDuration(Integer duration, Integer duration2) {
        return null;
        //return controlLogRepo.findByDurationBetween(duration, duration2);
    }
}
