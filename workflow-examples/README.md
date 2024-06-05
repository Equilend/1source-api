# workflow-examples

A set of examples exercising 1Source workflows

- application.properties drives all examples
- no local DB is required. Using H2 in memory.

## application.properties

Each example is controlled through property sets in application.properties. Uncomment/Comment depending on which example you're executing. This is the section for the Contract Proposal Example

\##### Contract Proposal #####  
\#com.os.auth.grant_type=password  
\#com.os.auth.client_id=canton-participant1-client  
\#com.os.auth.client_secret=c0a05c2d-ac70-472a-ac4f-38b80dba28d8  
\#com.os.auth.username=TestLender1User  
\#com.os.auth.password=fjmxVeKzpzUDg3YJ  
\#com.os.auth.contract_id=  
\#spring.batch.job.name=contractProposal  
\#############################  

Here is the section for the Return Proposal Example

/##### Return Notification #####  
com.os.auth.grant_type=password  
com.os.auth.client_id=canton-participant1-client  
com.os.auth.client_secret=c0a05c2d-ac70-472a-ac4f-38b80dba28d8  
com.os.auth.username=QABorrower1User  
com.os.auth.password=T5XwEEFazCPWqFcL  
com.os.auth.contract_id=02009e4c-bdf3-47b8-96e1-937043850382  
spring.batch.job.name=returnNotification  
/#############################  

Check application.properties for other job configurations.

## maven execution

clean spring-boot:run -Dspring-boot.run.jvmArguments="-Xms12288m -Xmx12288m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"

Spring boot will run the job spring.batch.job.name in application.properties.