# JDStruct

JDStruct is a fast library for creating structure and instances in a dynamic way  

This library features are : 
- Fast
- Memory efficient
- Avoid the use of reflection

**This library has been made for DadTDB, so all strcture must have a special key (id, timestamp or startDate&endDate)**

## Structure

A structure is a definition of multiple variable and has a fixed size

### Creation

Structure must be created using the StructureBuilder object  
Assume we want to build the following structure :  

```c
struct student {
	long id;
	int age;
	char surname[20];
	char firstname[20];
	float average;
}
```

The corresponding java code will be : 

```java
Structure student = StructureBuilder.withId()
	.putInt("id")
	.putInt("age")
	.putString("surname", 20)
	.putString("firstname", 20)
	.putFloat("average")
	.build()
```

The supported types are : 
- byte
- boolean
- short
- char
- int
- float
- long
- double
- String
- byte[]

### Structure serialization

```java
byte[] bytes = StructureIO.serialize(myStructure);
```

### Structure deserialization

```java
Structure structure = StructureIO.deserialize(bytes);
```

## Item

An Item is a structure instance  
Item can only be created using an ItemFactory

### Empty item creation

```java
Item item = factory.newItem();
```

### Parsing from Map<String, Object>

```java
Item item = factory.parse(map);
```

### Item serialization

```
byte[] data = item.toBytes();
```

### Item deserialization

```java
Item item = factory.parse(bytes);
```

## Item Factory

### Default

```java
ItemFactory factory = ItemFactory.from(structure);
```

### Advanced

Advanced ItemFactory can be created using ItemFactoryBuilder class  
This will add flexibility when parsing map objects

```java
ItemFactory factory = ItemFactoryBuilder.with(structure)
	.skip("field2")
	.transform("f1", "field_1").to("field1")
	.mandatory("field1")
	.build();
```
