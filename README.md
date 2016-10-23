## Inspiration
Machine learning has proved to be very helpful in solving today's problem . We wanted to take advantage of it and try to help the homeless by providing their own personal assistant for a better life. 
## What it does
Elixir gathers data from multiple websites such as the homeless directory and rent assistance. It uses this data to provide useful information for its users. Almost 80% of homeless people have access to government provided cellphones. The app acts like a server where where the user can interact with it via text message . The current functionalities are :
  * Rental Assistance Program provides contact info , address and name of nearby rental assistance program. This is useful for preventing someone from becoming homeless.
  * Emergency Shelters Near  By , this is great because it can provide updated information of shelters near by. This       also notifies the COC that the user has requested emergency shelter.
  * Soup Kitchens Near By , the app provide places where a user can get free food.
  * Services Job offers Near By , this feature allows the COC or a shelter to upload a job posting online or a service       such as free Swine flue or haircut. The user can then select this option to know more about these services.
  * Free Medical and Treatment Center, this will print out a list of near by free medical centers.

## How we built it
We built a server that handles sending ,receiving , and getting data. We build a data mining tool that gathers useful information from multiple online websites and databases. We built an application that handles text messaging between the users and the server. The server is made out of android , we chose android because it already has texting API's in it and was a lot faster than implementing twillio . We built a web app using an edu server  , java script  and fire-base.
## Challenges we ran into
 Our biggest challenge was trying to get IBM Watson to run properly. Our initial and still future thoughts is to implement IBM Watson to analyze data. This is specially helpful to predict someone who might become homeless. Give statistics of what the users are interested in. Help find solutions to problems from big data.

## Accomplishments that we're proud of
    * Managed to finish the user app in the duration of the hackathon.
    * Implemented a successful data mining tool to scrap data from big websites.
## What we learned

    * Learned how to use Fire base on the web and android.
    * Learned how to spawn and control threads for each client.
    * Learned how to create a web app.
    * Learned some java script.
    * Learned a ton about artificial intelligence.
    * Learned about reasons people become homeless, prevention and the lifestyle.
    * Learned about the many services that are provided to help fight homelessness.

## What's next for Elixir: Helps you stay or get back on your feet.
  * Implement AI to help analyze data.
  * Gather more information from the users for analytics.
  * Keep track of the user's activity and interests.
  * Implement a referral's system between COC and shelters.
