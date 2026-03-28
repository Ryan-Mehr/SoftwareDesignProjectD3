**FOR THE TAs (Teaching Assistants):**
1. How to run the program:
    1) Have IntelliJ and open project in IntelliJ.
    2) Have Docker and open Docker.
    3) Open the IntelliJ terminal open in this Project and enter the following command: docker compose up -d
    4) Have MySQL Workbench and open MySQL Workbench.
        1. Add a MySQL Connection by clicking on the '+' symbol at the bottom where it says 'MySQLConnection':
            * Connection name: [Can be anything]
            * Connection Method: Standard (TCP/IP)
            * Under Parameters:
                * Hostname: 127.0.0.1
                * Port: 3306
                * Username: root
                * Default Schema: [Leave blank]
        2. Connect to the MySQL Connection, password for 'root' is 'admin12345' and additional information can be found in the docker-compose.yml file.
        3. Once connected, click on 'Server' then 'Data Import'.
            * Click on 'Import from Self Contained File'.
            * Select the DB_Setup_V5.sql file.
            * Start Import
            * If you refresh on Schemas, you should see the new tables created.
    5) You are now ready to start the game through the Launcher.java file in the GUI folder.
2. How to run Test Cases:
    1) Find the test folder and find java>Tests>AppTest (Should be all highlighted in Green), right click on AppTest and click on 'Run AppTest'.
3. Other submission:
    1) Documents should be found in the EClass submission
    2) Video presentation is or will be in another EClass submission.


**HOW TO RUN AND DEVELOP:**
1. Use IntelliJ
2. Make sure you have SceneBuilder to make GUI with JavaFX: https://gluonhq.com/products/scene-builder/
3. Make sure you have MySQL Workbench so you can edit the MySQL DB using a GUI instead of having to do commands all the time.
4. Use docker to start up the database (you need it open running in the background before doing command):
  * docker compose up -d
7. Import the SQL file to construct the database structure.
  * Read SQL instructions sent in group chat.
8. Everything should be working as expected.
9. Make a new branch and work on that branch when doing development.

**GROUP MEMBERS:**
AtheenPahavathas, Anastasia Filipoiu, Ryan Mehr, Zarlasht Mikhail