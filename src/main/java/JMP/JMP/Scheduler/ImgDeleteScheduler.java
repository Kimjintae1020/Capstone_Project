package JMP.JMP.Scheduler;

import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImgDeleteScheduler {

    private final ResumeService resumeService;

    @Value("${file.dir}")
    private String uploadDir;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteImg() {

        // 1. 모든 이력서 파일 목록 조회
        List<Resume> resumeList = resumeService.getResumeAllList();

        // 2. 이미지 경로에 있는 이미지 목록 조회
        File path = new File(uploadDir);
        File[] files = path.listFiles();    // 현재 디렉토리에 있는 파일들을 배열로 변환
//        log.info("files = {}", files);      // src/main/resources/static/uploads/fc8bcb06-d344-4956-8253-c71f785bdc42.jpg

        Set<String> dbFileNames = new HashSet<>();

        for (Resume resume : resumeList) {
            String photoPath = resume.getPhoto();
            if (photoPath != null && !photoPath.isEmpty()) {
                String fileName = photoPath.substring(photoPath.lastIndexOf('/') + 1);
                log.info("fileName: " + fileName);
                dbFileNames.add(fileName);
            }
        }

        log.info("유효한 이미지 파일 개수 : " + dbFileNames.size()); // dbFileName -> 이미지가 있는 것

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileNameOnDisk = file.getName();
                    if (!dbFileNames.contains(fileNameOnDisk)) {
                        if (file.delete()) {
                            log.info(fileNameOnDisk + " 파일 삭제 성공");
                        } else {
                            log.warn(fileNameOnDisk + " 파일 삭제 실패");
                        }
                    }
                }
            }
        }

    }
}