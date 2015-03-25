IF EXISTS (SELECT * FROM sys.tables WHERE name='logging_event')
    DROP TABLE [logging_event]
GO

CREATE TABLE [logging_event] (
  [timestmp] BIGINT NOT NULL, 
  [formatted_message] TEXT NOT NULL,
  [logger_name] VARCHAR(255) NOT NULL,
  [level_string] VARCHAR(255) NOT NULL,
  [thread_name] VARCHAR(255) NOT NULL,
  [reference_flag] SMALLINT NOT NULL,
  [caller_filename] VARCHAR(255) NOT NULL,
  [arg0] VARCHAR(255),
  [arg1] VARCHAR(255),
  [arg2] VARCHAR(255),
  [arg3] VARCHAR(255),
  [caller_class] VARCHAR(255) NOT NULL,
  [caller_method] VARCHAR(255) NOT NULL,
  [caller_line] VARCHAR(255) NOT NULL,
  [event_id] INT IDENTITY NOT NULL ,
  PRIMARY KEY (event_id)
) 

GO

IF EXISTS (SELECT * FROM sys.tables WHERE name='logging_event_property')
    DROP TABLE [logging_event_property]
GO

CREATE TABLE [logging_event_property] (
  [event_id] INT NOT NULL, 
  [mapped_key] VARCHAR(255) NOT NULL,
  [mapped_value] TEXT NOT NULL
)

GO

IF EXISTS (SELECT * FROM sys.tables WHERE name='logging_event_exception')
    DROP TABLE [logging_event_exception]
GO

CREATE TABLE [logging_event_exception] (
  [event_id] INT NOT NULL, 
  [i] SMALLINT NOT NULL,
  [trace_line] VARCHAR(255) NOT NULL
)
