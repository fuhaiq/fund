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
      "type": "NUMERIC",
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

生成的schema如下
```sql
-- 创建 app 表
CREATE TABLE app (
    id BIGINT NOT NULL,
    name TEXT NOT NULL,
    author TEXT NOT NULL,
    price NUMERIC NOT NULL,
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    create_by TEXT NOT NULL,
    update_by TEXT NOT NULL,
    version INTEGER NOT NULL DEFAULT 1,

    -- 定义主键
    PRIMARY KEY (id)
);

-- 添加表注释
COMMENT ON TABLE app IS 'App应用信息';

-- 可选：为每个字段添加注释，便于理解与维护
COMMENT ON COLUMN app.id IS '主键ID';
COMMENT ON COLUMN app.name IS '应用名称';
COMMENT ON COLUMN app.author IS '应用作者';
COMMENT ON COLUMN app.price IS '应用价格（支持精确小数，如 NUMERIC(10,2) 如果需要可指定精度）';
COMMENT ON COLUMN app.create_time IS '创建时间，带时区，默认当前时间';
COMMENT ON COLUMN app.update_time IS '更新时间，带时区，默认当前时间';
COMMENT ON COLUMN app.create_by IS '创建人';
COMMENT ON COLUMN app.update_by IS '更新人';
COMMENT ON COLUMN app.version IS '版本号，默认为1';
```