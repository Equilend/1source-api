SELECT  STEP_EXECUTION_ID
        , JOB_EXECUTION_ID
        , STEP_NAME
        , STATUS
        , EXIT_CODE
        , bse.*
FROM batch_step_execution bse
WHERE 1=1
AND status IN ('STARTED', 'STARTING')
AND exit_code NOT IN ('COMPLETED', 'FAILED')
ORDER BY start_time desc
;

SELECT bje.JOB_EXECUTION_ID
        , bji.JOB_NAME
        , bje.JOB_INSTANCE_ID
        , bje.STATUS
        , bje.EXIT_CODE
        , bje.CREATE_TIME - current_timestamp as time_diff
        , bje.*
FROM batch_job_execution bje
INNER JOIN batch_job_instance bji
ON bje.job_instance_id = bji.job_instance_id
WHERE 1=1
AND status IN ('STARTED', 'STARTING')
AND exit_code NOT IN ('COMPLETED', 'FAILED')
ORDER BY bje.create_time desc
;

UPDATE 
    batch_step_execution 
SET END_TIME = current_timestamp
    ,LAST_UPDATED = current_timestamp 
    ,STATUS = 'COMPLETED' 
    ,EXIT_CODE = 'FAILED' 
WHERE 
    STEP_EXECUTION_ID in (
        select STEP_EXECUTION_ID 
        from batch_step_execution bse 
        where 1=1
        and status not in ('COMPLETED', 'FAILED')
        or EXIT_CODE not in ('COMPLETED', 'FAILED') 
    )
;

UPDATE 
    BATCH_JOB_EXECUTION 
SET 
    END_TIME = current_timestamp 
    ,LAST_UPDATED = current_timestamp 
    ,STATUS = 'COMPLETED' 
    ,EXIT_CODE = 'FAILED' 
WHERE 
   JOB_EXECUTION_ID in (
        select bje.JOB_EXECUTION_ID
        from batch_job_execution bje 
        where 1=1
        and (status not in ('COMPLETED', 'FAILED'))
        or (EXIT_CODE not in ('COMPLETED', 'FAILED'))
   )
;