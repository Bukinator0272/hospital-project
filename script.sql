DROP TABLE IF EXISTS doctor CASCADE;
DROP TABLE IF EXISTS patient CASCADE;
DROP TABLE IF EXISTS recipe CASCADE;

CREATE TABLE doctor
(
    doctor_id      INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    name           VARCHAR(100),
    surname        VARCHAR(100),
    patronymic     VARCHAR(100),
    specialization VARCHAR(100)
);

CREATE TABLE patient
(
    patient_id   INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    name         VARCHAR(100),
    surname      VARCHAR(100),
    patronymic   VARCHAR(100),
    phone_number VARCHAR(100)
);

CREATE TABLE recipe
(
    recipe_id          INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    description        VARCHAR(100),
    patient_id         INTEGER,
    doctor_id          INTEGER,
    creation_date      DATE,
    period_of_validity DATE,
    priority           VARCHAR(100),
    FOREIGN KEY (patient_id) REFERENCES patient (patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor (doctor_id)
);

INSERT INTO doctor (name, surname, patronymic, specialization)
VALUES ('�������', '����', '���������', '�������'),
       ('������', '��������', '���������', '�������'),
       ('��������', '���������', '�������������', '������'),
       ('������', '�������', '�������������', '������'),
       ('��������', '�������', '����������', '����������'),
       ('�����', '��������', '���������', '��������'),
       ('������', '������', '������������', '�������'),
       ('�������', '��������', '����������', '����������'),
       ('�������', '�����', '������������', '�������'),
       ('�������', '�����', '����������', '�������');

INSERT INTO patient (name, surname, patronymic, phone_number)
VALUES ('������', '��������', '����������', '5(1138)400-99-62'),
       ('�����', '�������', '��������', '680(419)714-48-80'),
       ('�������', '�������', '�����������', '480(281)916-88-87'),
       ('������', '�������', '����������', '77(2878)920-71-16'),
       ('��������', '�������', '�����������', '97(25)854-43-23'),
       ('�����', '����', '���������', '7(86)742-33-54'),
       ('��������', '������', '���������', '6(626)389-66-40'),
       ('��������', '��������', '����������', '8(400)878-61-10'),
       ('��������', '������', '��������', '65(311)759-55-52'),
       ('������', '����', '������', '5(99)902-87-15');

INSERT INTO recipe (description, patient_id, doctor_id, creation_date, period_of_validity, priority)
VALUES ('������ ������', 1, 1, '2001-01-09', '2002-03-15', '����������'),
       ('��������', 2, 2, '2002-06-23', '2002-06-25', '�������'),
       ('��������� �����', 3, 4, '2007-07-13', '2009-01-01', '�����������'),
       ('�������', 4, 4, '2011-12-30', '2012-01-10', '����������'),
       ('������ ��������������', 5, 5, '2017-06-19', '2029-11-25', '�������'),

       ('�����������', 6, 6, '2015-05-09', '2020-11-11', '�����������'),
       ('����� �� �����', 7, 7, '2020-01-06', '2021-01-06', '����������'),
       ('����� �������', 8, 8,'2020-03-08', '2023-03-08', '�����������'),
       ('������� �������', 9, 9, '2020-01-09', '2022-03-15', '����������'),
       ('����� �����', 10, 10, '2020-07-09', '2022-03-15', '�������'),

       ('��������� �����', 1, 10, '2011-05-19', '2076-01-17', '����������'),
       ('���������� �������', 2, 9, '2015-03-29', '2076-02-18', '�������'),
       ('�������� �����', 3, 8, '2018-04-24', '2076-04-22', '����������'),
       ('������ ���������', 4, 7, '2019-07-14', '2076-05-21', '�����������'),
       ('������ ���������', 5, 6, '2020-08-11', '2076-03-07', '�������'),

       ('��������', 6, 5, '2017-07-07', '2027-07-14', '�������'),
       ('������ �������������� ����', 7, 4, '2011-03-09', '2043-03-15', '����������'),
       ('������ ������� ����', 8, 3, '2001-09-11', '2021-04-16', '�������'),
       ('������� �������', 9, 2, '2020-04-04', '2020-11-25', '����������'),
       ('��������� ����', 10, 1, '2020-10-15', '2022-03-15', '�����������');