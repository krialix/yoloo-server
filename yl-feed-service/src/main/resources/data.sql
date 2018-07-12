INSERT INTO AUTHOR (ID, DISPLAY_NAME, PROFILE_IMAGE_URL)
  VALUES (1000, 'Test User', 'http://localhost/sdsdf.jpg');

INSERT INTO POST_GROUP (ID, DISPLAY_NAME)
  VALUES (12, 'GROUP 1');

INSERT INTO POST (ID,
                  APPROVED_COMMENT_ID,
                  BOUNTY,
                  CONTENT,
                  CREATED_AT,
                  DELETED_AT,
                  FLAGS,
                  MEDIAS,
                  TAGS,
                  TITLE,
                  AUTHOR_ID,
                  POST_GROUP_ID)
  VALUES (125,
        NULL,
        0,
        'Hello World',
        '2014-04-28 16:00:49.455',
        '2014-04-28 16:00:49.455',
        '[]',
        '[]',
        '[]',
        'Title 1',
        1000,
        12)