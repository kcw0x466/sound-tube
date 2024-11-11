const express = require('express');
const { spawn } = require('child_process');
const app = express();
const PORT = 8000;

// 유튜브 영상에서 음원을 추출하여 HTTP로 스트리밍하는 엔드포인트
app.get('/stream', (req, res) => {
    // 유튜브 URL을 쿼리 파라미터로 받음
    const youtubeUrl = req.query.url;

    if (!youtubeUrl) {
        return res.status(400).send('You must provide a YouTube URL');
    }

    // yt-dlp 명령어로 유튜브 영상에서 오디오만 추출
    const ytDlp = spawn('C:/ffmpeg/bin/yt-dlp.exe', [
        '-f', 'bestaudio', // 가장 좋은 오디오 품질
        '--extract-audio', // 오디오만 추출
        '--audio-format', 'mp3', // MP3 형식으로 저장
        '--audio-quality', '0', // 최고 품질
        '-o', '-', // 출력 경로는 stdout으로 설정
        youtubeUrl
    ]);

    // ffmpeg로 오디오 스트리밍을 HTTP로 전달
    const ffmpeg = spawn('C:/ffmpeg/bin/ffmpeg.exe', [
        '-i', '-', // yt-dlp의 출력 스트림을 입력으로 받음
        '-vn', // 비디오 제외
        '-acodec', 'libmp3lame', // MP3로 변환
        '-f', 'mp3', // MP3 포맷
        'pipe:1' // 표준 출력으로 결과를 스트리밍
    ]);

    // yt-dlp의 표준 출력을 ffmpeg에 전달
    ytDlp.stdout.pipe(ffmpeg.stdin);

    // ffmpeg의 출력을 HTTP 응답으로 스트리밍
    ffmpeg.stdout.pipe(res);

    // 에러 처리
    ytDlp.stderr.on('data', (data) => {
        console.error(`yt-dlp error: ${data}`);
    });
    ffmpeg.stderr.on('data', (data) => {
        console.error(`ffmpeg error: ${data}`);
    });

    // 스트리밍 종료 시 처리
    ytDlp.on('close', (code) => {
        console.log(`yt-dlp exited with code ${code}`);
    });

    ffmpeg.on('close', (code) => {
        console.log(`ffmpeg exited with code ${code}`);
    });
});

// 서버 시작
app.listen(PORT, () => {
    console.log(`Server is running at http://localhost:${PORT}`);
});
