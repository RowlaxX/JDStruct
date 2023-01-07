# JDStruct

JDStruct is a fast library for creating structure and instances in a dynamic way  

This library features are : 
- Fast
- Memory efficient
- Avoid the use of reflection

This library has been build with 

## Structure

A structure is a definition of multiple variable and has a fixed size

### Creation

Structure must be created using the StructureBuilder object  
Assume we want to build the following structure :  

'''c
struct student {
	long id;
	int age;
	char surname[20];
	char firstname[20];
	float average;
}
'''

'''java
Structure student = StructureBuilder.withId()
	.putInt("age")
	.putString("surname", 20)
	.putString("firstname", 20)
	.putFloat("average")
	.build()
'''

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

### Serialization

'''java
byte[] bytes = Structure.serialize(myStructure);
'''

### Deserialization

'''java
Structure structure = Structure.deserialize(bytes);
'''

## Member

A member correspond to a field in the structure  
Member cannot be created by the user

## Item

An Item is a structure instance  
Item can only be created using an ItemFactory

### Creation

#### Blank

'''java
Item item = factory.newItem();
'''

#### Parsing

Parsing can be done using a Map<String, Object> instance

'''java
Item item = factory.parse(map);
'''

### Serialization

'''
byte[] data = item.toBytes();
'''

### Deserialization

'''java
Item item = factory.parse(bytes);
'''

## Item Factory

ItemFactory can create item

### Creation

#### Default

'''java
ItemFactory factory = ItemFactory.from(structure);
'''

#### Advanced

Advanced ItemFactory can be created using ItemFactoryBuilder class  
This will add flexibility when parsing map objects

'''java
ItemFactory factory = ItemFactoryBuilder.with(structure)
	.skip("field2")
	.transform("f1", "field_1").to("field1")
	.mandatory("field1")
	.build();
'''
