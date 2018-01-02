For anybody's information:

You're looking through the tasks and probably thinking: "Why the hell are there random letters/numbers in front
of the tasks?" 

Well, those numbers and letters are actually my way of organizing the tasks to be in the order that I want them to be in.
I didn't realize until later that this is one of the best ways, instead of writing out manual indexes in every file,
which is just time consuming. But yeah, if you want to add more tasks for a certain area, open up "aOnTheRun.txt" and 
look through that file or any other task-related file. You will realize how easy it actually is to add tasks. All you
have to do is add the file and fill it out (use "aOnTheRun.txt" as reference) and add in a bit of code giving the player
a reward at the point of completion. For example, if I want to add a task of: cutting a willow log, I would create
a new file and you can name it whatever you want but i'm naming it: "Pr0Will0wCh0pper.txt" and you would fill it out like
this:


Pr0 Willow Ch0pper - Easy - 1 - 995, 1000, 1353, 1

Chop a willow tree and get some willow logs.

<col=ffff00>You will need a hatchet for this task and level 30 Woodcutting.

1,000 coins and a steel hatchet



And done! Now you have the base for a task. There's literally only one thing left to add, and that is the part
where the player gets the rewards once they chop down a willow tree and get some willow logs. Head over to Woodcutting.java
and find "you get some". Below that, add this bit of code:

if(!player.tasks().hasFinished(player.tasks().ldTasks(), (byte) player.tasks().indexFromName("pr0will0wch0pper")))
	killer.tasks().taskCompletion("pr0will0wch0pper");
	
And you're done!

An Added note: For anybody wondering what this line means: Pr0 Willow Ch0pper - Easy - 1 - 995, 1000, 1353, 1
Well, the "Pr0 Willow Ch0pper" represents the name of the task, the "Easy" represents the task's difficulty, 
the first "1" represents how many points you get from completing that certain task, the "995, 1000" represents
the item id and amount of the reward the player would receive from completing this task, and the "1351, 1"
represents the same thing. You can add as many items as you want as a reward for completing a certain task.
Also, anybody wondering what the code "ldTasks()" above means, it represents the boolean array of tasks
from the Lumbridge and Draynor set. Well, that's all! Enjoy your new task system.