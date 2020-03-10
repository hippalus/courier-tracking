# courier-tracking

##This project contains 2 modules.
  ####1.module courier-geo-locations
  It generates the time series-shaped courier location information and send it to the kafka topic.
  
  This module feeds the courier-event-processor module.
  
  To produce accurate and consistent data.
  
  See to https://nmeagen.org/
  
  ####2.module courier-event-processor
  This module processes and saves courier location data in real time.
  
  Logs the couriers' entry events to the  stores
  
  From Kafka topic  processes the data consumes according to the conditions.
  
##How to run?
  ### go to courier-tracking root directory
     mvn clean install
     
     sh run.sh
  
    
      

  
