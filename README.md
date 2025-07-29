# Sound Tube
- Youtube 기반 음악 스트리밍 플레이어 앱
- 컴퓨터공학과 3학년 2학기 안드로이드 프로그래밍 과제로 수행한 프로젝트

## Tech Stack
### Server
- Javacript
- Node.js - Express
- FFmpeg
### Clinet (Android Project)
- Java
- Android API
- SQLite (DB)

## 기능
### Server
- 음원 스트리밍: 클라이언트(앱)에서 서버로 유튜브 영상 링크를 GET 요청으로 보내면 그 영상을 음원으로 변환 후 클라이언트(앱)로 스트리밍 
- 음원으로 재생될 영상 정보 제공: 클라이언트(앱)에서 서버로 유튜브 영상 링크를 GET 요청으로 보내면 그 영상의 정보(유튜브 영상 ID, 제목, 영상길이)를 클라이언트(앱)한테 제공
### Clinet (Android Project)
- 전체 노래 목록 재생: 저장된 노래 목록들을 순차적으로 재생
  - 버튼으로 이전 곡 / 다음 곡 재생 가능
- 퀵플레이 재생: 유튜브 영상 링크 입력 후 바로 재생
- 노래 추가:  유튜브 영상 링크 입력으로 그 영상 에 해당하는 정보들을 가져와서 SQLite DB에 저장
- 노래 삭제: 저장된 노래들중에서 선택된 노래들을 SQLite DB에서 삭제

## Showcase
### 전체 노래 목록 화면 (앱 시작 화면)
![image](https://github.com/user-attachments/assets/1c15407e-560a-42db-8468-76e1f3e6b0ed)

### 퀵플레이 화면
![image](https://github.com/user-attachments/assets/463bb2c7-c8b7-4a0d-a95f-367d7da3e295)

### 현재 버그 리스트
클라이언트(앱)
- 다음/이전 곡 버튼 연속으로 빠르게 누를때 뮤직 리스트 인덱스 이상 현상 생김
- 예외처리 제대로 안됨 (토스트 메세지 출력 x)
서버 



