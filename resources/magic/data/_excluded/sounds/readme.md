### bong.wav
original wav which was found not to play correctly in Linux Mint 17.3 because it was [too short](http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4434125).


#### fix: increasing length of wav (on linux)
    # make a 1 second wav of silence.
    ffmpeg -filter_complex aevalsrc=0 -t 1 1Sec.wav
    
    # concatenate the silence to the end of original sound file.
    ffmpeg -i bong.wav -i 1Sec.wav -filter_complex '[0:0][1:0]concat=n=2:v=0:a=1[out]' -map '[out]' output.wav
    
    # convert to 16kHz sample.
    avconv -i output.wav -ar 16000 new-bong.wav        