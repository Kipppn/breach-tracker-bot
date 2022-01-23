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

The bot was built in Java using JDA. Like [pwnedBot](https://github.com/plasticuproject/pwnedBot), Breach Tracker Bot uses the [Have I Been Pwned](https://haveibeenpwned.com/) API to retreve breach information.

## Challenges we ran into

###


## Accomplishments that we're proud of

## What we learned

## What's next for Breach Tracker Bot
