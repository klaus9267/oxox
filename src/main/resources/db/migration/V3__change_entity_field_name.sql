ALTER TABLE reactions
RENAME COLUMN reactionEmoji TO emoji;

ALTER TABLE votes
RENAME COLUMN isYes TO is_yes;