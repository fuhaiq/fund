# 创建表

AI 提示：
我已经在postgres里面安装好了uuidv7扩展，可以使用如下语法创建一个id字段，并指定默认值
```sql
id uuid NOT NULL DEFAULT uuid_generate_v7(),
```
请创建一个表，基于postgres数据库，表结构的json格式如下

```json
{
  "tableName": "app",
  "comment": "App应用信息",
  "columns": [
    {
      "name": "name",
      "type": "text",
      "required": true
    },
    {
      "name": "author",
      "type": "text",
      "required": true
    },
    {
      "name": "price",
      "type": "number",
      "required": true
    },
    {
      "name": "id",
      "type": "BIGINT",
      "required": true,
      "primaryKey": true,
      "default": false
    },
    {
      "name": "create_time",
      "type": "timestamptz",
      "required": true,
      "timezone": true,
      "default": true
    },
    {
      "name": "update_time",
      "type": "timestamptz",
      "required": true,
      "timezone": true,
      "default": true
    },
    {
      "name": "create_by",
      "type": "text",
      "required": true,
      "default": false
    },
    {
      "name": "update_by",
      "type": "text",
      "required": true,
      "default": false
    },
    {
      "name": "version",
      "type": "number",
      "required": true,
      "default": 1
    }
  ]
}
```