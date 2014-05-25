package com.remembook.photo;

import java.util.Arrays;
import java.util.List;

public class PhotoGalleryReference {
    public static final int NUM_OF_COLUMNS = 3; //그리드 한줄에 표시괼 이미지 수
    public static final int GRID_PADDING = 8; //그리드 이미지간 패딩
    public static final String PHOTO_ALBUM = "RememBook"; //저장될 장소
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png"); //지원하는 확장자
}