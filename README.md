Task 1

Pattern Used MVVM

 SDK  
   1. TesseractAppListSDK :- 
           AppListBaseLayer is Singletom class is the Entry point for SDK. This SDK provides App name, Package name, Icon, Main Activity class
name, Version code, and Version name to Business Layer.
  2. Sorted Packages Name with respect to App Name
   
 App - UI 
     1. Get Data from the TesseractAppListSDK and display on RecyclerView
     2. Provide Images to all Packages at BL Layer
     3. Launching application when click on any Package
     4. Get AIDL Sensor Data Button :- Provides data using AIDL connection with Task 2.
     5. Implemented Search Functionality
  
  
Notify when app installs/uninstalls. 
    1) Run Background service to Achieve if new applciation install or delete installed app




