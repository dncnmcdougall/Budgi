Installation:
Just extract this archive.
For windows:
	Rename the 2 files from Budget.bat2 to Budget.bat and check.bat2 to Check.bat
	Run the Budget.bat file.
	This will launch the program.
	Note you must have java (JRE) 1.5.0 or higher installed
	To check this run check.bat
		If the output says something like: “command not found” you need to install java JRE.
		It it is installed the version will be displayed.
	I recommend creating a shortcut to the Budget.bat file and run that 	instead.
For Linux:
	Run run.sh Unfortunately this will open a terminal window.
	Alternatively create a launcher with the command:
	java -jar /<extracted path>/Budget.jar 
		Note: This will result in the Saves folder being created in you home folder.
	If you find a better way to do this please tell me.


Usage:
Note this program will create a sub directory Saves in the folder in which it is run, and a sub directory in that called Old.
In windows, by default, java runs from your home directory, so this is where the Saves folder will be created

In Saves a file will be created called Current.zip
This is the file where the current 'running' data is saved to and loaded from.
The program loads from it on startup and saves changes to it on File/save and on exit.

In Saves/Old files with a date.zip will be saved. These are the archive files.
A possible way to load them is to rename them to Current.zip and put them in Saves. (just back up all the appropriate files before you do this to prevent data loss)
Note: you can only archive once a day. Any subsequent archives will save over the old ones. The file name for and archive is: the current date .zip
This could be changed in future if required.

(Yes they are zip files that you can open as compressed files. Inside are extensionless files, these can be viewed as normal text files.)

A better way to load and view old files will come later.

other usage documentation will come later.

V1.01
Minor bug fix: dates no longer move forward with subsequent saves
The dates all used to be saved one month ahead. This mean that if you saved twice then the month that an expense was spent on would be two months ahead of the current.
Note that this only affected the month in the date not the year (unless the month went over the end of the year) or the day

V1.01a
Added the capacity to change what currency is displayed. (a prefix and a suffex can now be customised)
I Cannot remember the all changes I made here.
Minor usability fixes and operational fixes.

V1.02
Percentages are now between 0 and 100. (Internally and in the save files they are still 0 and 1);
Archives now save with time in the name as well, so that you can archive multiple times in a day.
When adding a new element text box's contents are highlighted.
Some minor GUI resizes.

V1.03 Alpha
Added the capacity to read configurations from the config file stored in Saves/
Added the config that tells the program where to read save files from. This directory stores Current.zip and Old/ for archives. (Same as what Saves used to do)
Added edit menu. Moved options there. Added menu item (to edit menu): to edit config.
Added ConfigDialog class.   

V1.3
Changed version number to 1.3 from 1.03. This better reflects the maturity of the program.
Added a 'Ver' to the config file. This represents the version number.
Changed background colour in Remove Dialog.
Changed font of Remove Dialog to match other fonts.
Changed size of Remove Dialog.
Moved Add, Remove, Show Total, and Refresh from the File menu to the Edit Menu. 
Added a Help menu.
Added an about screen to the Help menu
Changed the load method to allow for loading on non-current files
Added the capability to export to .csv

V1.3.1
Minor Update. Fixing bugs.
Fixed bug: program now saves resized size.
Added more info when exporting.
Added version checking.

1.4.0
Major Update. Fixing bugs, adding features.
Fixed Bug: Program crashes in December.
Fixed Bug: When Removing a 'Main Savings Account' the current main savings account is set back to null.
Changed: Export delimiter is now a comer not a semicolon.
Added: Show progress. A different colour on the allowance and expense elements showing where we are in the month relative to how much you have spent.
Changed: The fonts of the text displayed.
Added: The capacity for old save files to be loaded. (Backwards compatability) 
Added: The capacity to edit 'spent' items in the View dialog.
Added: The capacity to change the total on a carry over account.

1.4.1
Minor Update. Fixing bugs
Fixed Bug: exports Januarry's date properly.
Fixed Bug: set config version to 1.4.1
Fixed Bug: Rounded numbers to 2 decimal places to avoid double arithmetic errors.

Author:
Duncan McDougall
dncn.code@gmail.com
