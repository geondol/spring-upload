package hello.upload.file;

import hello.upload.domain.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {

        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()){
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()){
            return null;
        }

        String originFilename = multipartFile.getOriginalFilename();
        //image.png

        //서버에 저장하는 파일명
        String storeFileName = createStoreFileName(originFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originFilename,storeFileName);
    }

    private String createStoreFileName(String originFilename) {

        //마지막 . 뒤에오는 확장자명 뽑기 메소드 png
        String ext = extractExt(originFilename);

        //서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();

        String storeFileName = uuid + "." + ext;
        return storeFileName;
    }

    private String extractExt(String originFilename) {
        int pos = originFilename.lastIndexOf(".");
        return originFilename.substring(pos + 1);
    }


}
