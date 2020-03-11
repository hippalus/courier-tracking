# courier-tracking

## This project contains 2 modules.

  #### 1.module courier-geo-locations
  
  It generates the time series-shaped courier location information and send it to the kafka topic.
  
  This module feeds the courier-event-processor module.
  
  To produce accurate and consistent data.
  
  See to https://nmeagen.org/
  
  ![Atasehir-Courier1-Rota](https://user-images.githubusercontent.com/17534654/76303031-1698fa80-62d2-11ea-86a3-b152d561b686.png)

  
  #### 2.module courier-event-processor
  
  This module processes courier location data in real time and saves it to ElasticSearch.
  
  Logs the couriers' entry events to the  stores
  
  From Kafka topic  processes the data consumes according to the conditions.
  
## How to run?
  ### Requeriment
      
      java version 11
      
      Docker
      
  ### Go to courier-tracking root directory
  
     mvn clean install
     
     sh run.sh
     
 ### Check out the development branch to try locally
 
     git checkout origin/development
     
## MY TODO LIST 
  - Refactoring and add common module.
  
  - More integration and unit tests.
  
  - Segregate application profiles. For example prod-profile test-profile dev-profile container-profile.
  
  
  
    
      

  
