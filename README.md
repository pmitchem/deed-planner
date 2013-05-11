deed-planner
============

Deed planner/house calculator for Wurm Online - public repository.

Program official forum thread: http://forum.wurmonline.com/index.php?/topic/79352-deedplanner-3d-house-and-deed-planner/

This is Java 7 project - it will does not work with older versions of JDK!

To be able to run and edit this program, please follow this steps:
1. If you don't have it, download and install NetBeans. You can use another IDE's, but GUI is made in NetBeans, so you need NetBeans if you want to make any changes in it.
2. Download and attach two libraries: latest version of LWJGL ( http://www.lwjgl.org/ ) and TWL PNGDecoder ( http://twl.l33tlabs.org/dist/PNGDecoder.jar )
3. download latest official release of DeedPlanner from program thread ( http://forum.wurmonline.com/index.php?/topic/79352-deedplanner-3d-house-and-deed-planner/ ) and place folder "Data" and file "version.txt" in program working directory.

Code is not documented, but volunteers can document it, though the code should be the best documentation for itself. ;)

Project goals:
1. Creation of the most advanced, professional-looking deed planner for Wurm Online
2. Access to the widest possible audience - as little platform-dependend code as possible
3. The program usage should be as simple as possible
4. Cooperation with other Wurm-related programs and (possibly in the future) the game itself.

The program main class is located in Form/HouseCalc.java.

The program have modular structure with core in Mapper/Mapper.java.
