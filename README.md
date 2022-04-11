# swe-project-team-3

Iryna :), Keeton, Will and Will's project for CMSC B353 - Software Engineering

## Developing

1. Install MongoDB following [these instructions](https://docs.google.com/document/d/1Zno0zaUld6j1pwMh8A7qgR08L2TUEyfpyA8ntWu4cBY/edit#).
2. Start the database.
  * on macOS: `myMongoInstallation/bin/mongod --dbpath swe-project-team-3/db`
  * on Windows: `myMongoInstallation\bin\mongod --dbpath swe-project-team-3\db`
3. Make sure nodemon is installed globally: `npm install -g nodemon`
4. Make sure you have the template rendering tool is installed _inside the scholarshipr folder_: `npm install ejs`
5. Install dependencies: `npm install`
6. Start the express server: `npm run start` or `nodemon index.js`
7. Go to http://localhost:3000

## Database / Mongo Problems
Sometimes I get a database error when I try to launch my mongo installation on that certain port. It seems the workaround is to view the processes currently running and then kill the mongo one by id (all using grep in terminal). Chris shared some instructions:
`ps -ef | grep mongo`
`kill 58431`
Or kill whichever one has the id you want to kill