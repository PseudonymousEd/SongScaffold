Plan for adding audio chords to Song Scaffold

Overview:
We are adding new functionality to Song Scaffold

When the user sees the final song idea,
The user will be able to press the play button and play the chord progression.
The chord progression will continue to loop until the user stops or pauses the progression.
If there is a second chord progression, then the user will have the ability to go to the next chord progression.
The 2nd chord progression will play in the same manner.
If there is a third chord progression, the user will be able to go to that one as well.
From the 3rd progression, the user will be able to go back to the first progression.


Final Song Idea screen:
These new buttons only show up if Chord progressions are enabled in settings.
Add new buttons - Play/Pause (1st) (2nd) (3rd)
Pressing Play will start the chord progression (or resume if it was paused). While playing, the button becomes "Pause"
Pressing Pause will pause the chord progression. While no chords are playing, the button becomes "Play"
(1st) is an optional button that only appears if the 2nd or 3rd chord progression is playing. If selected, after the current chord progression is complete, the next progression to be played will be the 1st.
(2nd) is an optional button that only shows up if there is a 2nd chord progression and it isn't being played. If selected, after the current chord progression is complete, the next progression to be played will be the 2nd.
(3rd) is an optional button that only shows up if there is a 3rd chord progression and it isn't being played. If selected, after the current chord progression is complete, the next progression to be played will be the 3rd.

If a button to change a chord progression is hit right after another button is hit, the most previous button is the one that will be used to determine the next chord progression.

Settings Screen:
Beats Per minute setting - user may enter in the beats per minute that the chord progression will play. Default should be 60.