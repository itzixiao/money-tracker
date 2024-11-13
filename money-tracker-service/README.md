<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">记账APP(v1.0.0)</h1>
<h4 align="center">基于SpringBoot+Vue前后端分离的Java快速开发框架</h4>

## 简介

随着现代生活节奏的加快和消费模式的多样化，个人理财和账目管理已成为每个人日常生活中不可或缺的一部分。为了高效、便捷地管理日常账务，我们开发了一款基于RuoYi-App、SpringBoot、VUE3和TypeScript技术栈的记账应用。该记账APP不仅提供简洁的财务记录和分析功能，还特别加入了导入账单的便捷功能，支持通过微信、支付宝等平台导入账单，用户可以轻松上传CSC、ZIP格式的账单文件，快速同步各类支付记录，极大提升了用户的使用体验。

在本项目中，RuoYi-App作为系统的基础框架，提供了稳定的权限管理和快速开发的能力，确保了应用的高效开发和稳定运行。后端采用了SpringBoot技术栈，具备高度的可扩展性和强大的API支持，能够支持大规模的数据处理和灵活的功能拓展。前端则采用了VUE3和TypeScript，通过现代化的前端开发模式，提供了优雅的用户交互和流畅的操作体验。

本记账APP旨在简化财务管理流程，使用户能够轻松记录、分类和分析支出，帮助他们更好地掌握自己的财务状况，从而做出更加理智和科学的财务决策。


## 模块功能说明
```lua
├─deploy
│  └─money-tracker-admin  # 部署文件夹，包含项目部署脚本
├─doc
│  ├─data  # 数据相关文档和配置文件
│  ├─script  # 脚本文件，备份sql脚本
│  └─sql
│      └─design  # SQL 设计文档，包括数据库表结构设计等
└─money-tracker-admin
│   │─src
│   │   ├─main
│   │   │  ├─java
│   │   │  │  └─com
│   │   │  │  │   └─jcwl
│   │   │  │  │   │   └─admin
│   │   │  │  │   │   │   ├─account    # 账户管理模块
│   │   │  │  │   │   │   ├─book       # 记账管理模块
│   │   │  │  │   │   │   ├─category   # 分类管理模块
│   │   │  │  │   │   │   ├─common     # 公共工具类和常量
│   │   │  │  │   │   │   ├─minio      # MinIO 存储服务相关代码
│   │   │  │  │   │   │   ├─system     # 系统管理模块
│   │   │  │  │   │   │   ├─version    # 版本管理模块
│   │   │  │  │   │   │   ├─websocket  # WebSocket 通信模块
│   │   │  │  │   │   │   └─wechat     # 微信相关功能模块
│   │   │  └─resources
│   │   │  │   ├─i18n      # 国际化资源文件
│   │   │  │   ├─mapper    # MyBatis 映射文件
│   │   │  │   │  ├─account  # 账户管理模块的映射文件
│   │   │  │   │  ├─book     # 记账管理模块的映射文件
│   │   │  │   │  ├─category # 分类管理模块的映射文件
│   │   │  │   │  ├─system   # 系统管理模块的映射文件
│   │   │  │   │  ├─version  # 版本管理模块的映射文件
│   │   │  │   │  └─wechat   # 微信相关功能模块的映射文件
```

### 描述
* 存储层：主要包含数据存储组件，如关系型数据库、NoSQL数据库等。该层负责数据的持久化存储和管理，提供数据访问和操作的能力。
* 服务层：包含业务逻辑和数据处理的服务组件。这些服务组件基于微服务架构进行设计，可独立部署和扩展。它们处理来自应用层的请求，执行业务逻辑，并与其他服务或存储层进行交互。
* 接口层：也称为API层，包含一系列的RESTful API。这些接口提供了外部应用与系统内部服务进行交互的途径。它们封装了服务层的功能，使得外部调用更加简洁和标准化。
* 访问层：这是系统的最外层，直接与用户交互。它包含前端应用（如Web应用、移动应用等）和相关的控制器组件。这些控制器负责接收用户的请求，调用接口层进行业务处理，并返回相应的响应给用户。

## 代码提交规范
### `<type>(<scope>): <subject>`
#### • type：描述本次提交的类别，使用下面标识
* ○ feat：新功能（feature）
* ○ fix：修补bug
* ○ docs：文档（documentation）
* ○ style： 格式（不影响代码运行的变动、空格、格式化等等）
* ○ refactor：重构（即不是新增功能，也不是修改bug的代码变动）
* ○ perf: 性能 (提高代码性能的改变)
* ○ test：增加测试或者修改测试
* ○ build: 影响构建系统或外部依赖项的更改(maven,gradle,npm 等等)
* ○ revert: Revert a commit
#### • scope：描述本次提交影响的范围，或者版本
#### • subject：本次提交内容的简短描述


## Docker构建和运行
### 构建Docker镜像：
```
docker-compose build
```
### 启动服务：
```
docker-compose up -d
```

### 停止和清理
#### 停止服务：
```
docker-compose down
```
#### 删除卷：
```
docker volume rm money-tracker-admin_db-data money-tracker-admin_minio-data
```
   