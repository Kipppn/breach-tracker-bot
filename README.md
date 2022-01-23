# Breach Tracker Discord Bot


Breach Tracker Bot is a discord bot that periodically sends information about any new breaches and pings any discord user in the server whose accounts or emails were in these breaches. 

### Commands

-help: lists out all of the commands

-track: notifies the bot that you want an account to be tracked in a breach and dms you if the account was in a recent breach every week

-removetrack: notifies the bot you no longer want to recieve dms about a certain account

-setchannel: sets a text channel where you want your server to recieve breach info weekly


## Inspiration

One inspiration for Breach Tracker Bot was [pwnedBot](https://github.com/plasticuproject/pwnedBot). PwnedBot is a discord bot that allows you to check if emails, passwords, or account names have been breached using an api of the website known as [Have I Been Pwned](https://haveibeenpwned.com/).

We thought that automating the process of periodically checking your breached accounts would improve privacy accessibility because many people only really check if they notice a few signs of their accounts being hacked. Although a few services do email you when breaches occur, many people use discord and get notifications easier from it. 


## How we built it

The bot was built in Java using JDA. Like [pwnedBot](https://github.com/plasticuproject/pwnedBot), Breach Tracker Bot uses the [Have I Been Pwned](https://haveibeenpwned.com/) API to retreve breach information. It also uses Google GSON to parse api calls.

## Challenges we ran into

### Periodic scheduling

To schedule the periodic discord dms and messages, we used Java's Date, Timer, and TimerTask classes. This periodic scheduling took much longer than we expected due to the many bugs that we came across. 

### The set interval command

We originally had a command that allowed you to set the interval at which it would send tracking and dm information. This also proved to be much more difficult that we anticipated due to the bugs with the periodic scheduling. Also we realized we would have to store interval information for each server that had the bot much later in the hackathon. Due to this, we decided to scap the command and just set the interval to be a constant one week.

### HIBP API 

After many hours into the hackathon, we realized that the Have I Been Pwned api wasn't free like we thought it was. We analyzed whether it would be worth it to continue with this idea and came to the conclusion that we would just pay one month for this hackathon and see if we want to continue later. Authenication for the Have I Been Pwned API was an extreme pain point for us as well. We had many bugs with adding it to the header of the request.

We also unfortunatly had some issues with fetching breach information so only specific emails are tracked and dms. Typing the setchannel command will produced the message "No new Breaches since {date}" every time its supposed to check for new breaches.
## Accomplishments that we're proud of

- We ended up getting the periodic task scheduler working with minimal bugs
- All the commands provide a UI of some sort
- Checking if a users email adresses have been breached recently works!

## What we learned
- Anticipate that errors and give ample time to fix them
- When to cut features if they aren't going to meet deadlines
- If deadlines are approaching, you will have to go the quick and dirty way.

## What's next for Breach Tracker Bot
- Store data in some database to make sure that if the bot goes down, users wont have to reenter the track command
- Host the bot in the cloud so that the bot is on 24/7
- Fetch Account names rather than just emails
- Make sure emails are valid 
- Fetch breach information to send to discord text channel
