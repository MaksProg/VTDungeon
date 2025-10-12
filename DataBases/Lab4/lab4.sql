SELECT Н_ЛЮДИ.ФАМИЛИЯ, Н_СЕССИЯ.УЧГОД
FROM Н_ЛЮДИ
RIGHT JOIN Н_СЕССИЯ ON Н_ЛЮДИ.ИД = Н_СЕССИЯ.ЧЛВК_ИД
WHERE Н_ЛЮДИ.ФАМИЛИЯ > 'Соколов'
  AND Н_СЕССИЯ.ЧЛВК_ИД > 100622;

CREATE INDEX idx_сессия_члвк_ид ON Н_СЕССИЯ USING btree(ЧЛВК_ИД);
CREATE INDEX idx_люди_фамилия ON Н_ЛЮДИ USING btree(ФАМИЛИЯ);
CREATE INDEX idx_люди_ид ON Н_ЛЮДИ USING btree(ИД);


SELECT Н_ЛЮДИ.ФАМИЛИЯ, Н_ВЕДОМОСТИ.ИД, Н_СЕССИЯ.ДАТА
FROM Н_ЛЮДИ
RIGHT JOIN Н_СЕССИЯ ON Н_ЛЮДИ.ИД = Н_СЕССИЯ.ЧЛВК_ИД
JOIN Н_ВЕДОМОСТИ ON Н_СЕССИЯ.СЭС_ИД = Н_ВЕДОМОСТИ.СЭС_ИД
WHERE Н_ЛЮДИ.ОТЧЕСТВО < 'Александрович'
  AND Н_ВЕДОМОСТИ.ДАТА < DATE '2010-06-18'
  AND Н_СЕССИЯ.УЧГОД = '2008/2009';

CREATE INDEX idx_сессия_члвк_ид ON Н_СЕССИЯ USING btree(ЧЛВК_ИД);
CREATE INDEX idx_люди_ид ON Н_ЛЮДИ USING btree(ИД);
CREATE INDEX idx_сессия_сэс_ид ON Н_СЕССИЯ USING btree(СЭС_ИД);
CREATE INDEX idx_ведомости_сэс_ид ON Н_ВЕДОМОСТИ USING btree(СЭС_ИД);
CREATE INDEX idx_люди_отчество ON Н_ЛЮДИ USING btree(ОТЧЕСТВО);
CREATE INDEX idx_ведомости_дата ON Н_ВЕДОМОСТИ USING btree(ДАТА);
CREATE INDEX idx_сессия_учгод ON Н_СЕССИЯ USING btree(УЧГОД);
