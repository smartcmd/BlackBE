# 🛡️ BlackBE Plugin for Allay

> 一个强大的 Minecraft 服务器反作弊插件，自动检查玩家是否在 [BlackBE](https://blackbe.work) 数据库中被记录

[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://www.oracle.com/java/)
[![Allay](https://img.shields.io/badge/Allay-0.18.0-blue.svg)](https://github.com/AllayMC/Allay)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 📖 简介

BlackBE 是一个为 Allay 服务器平台开发的插件，用于自动检测和拦截在 BlackBE 黑名单数据库中被记录的玩家。本插件原本是为 Endstone 平台使用 Python 开发的，现已完全移植到 Allay 平台，使用 Java 重写。

## ✨ 功能特性

- 🚫 **自动拦截** - 玩家加入时自动查询 BlackBE 数据库，拒绝黑名单玩家进入服务器
- 🔍 **手动查询** - 管理员可通过命令查询任意玩家的黑名单状态
- ⚡ **异步处理** - 所有 HTTP 请求均为异步，不会阻塞服务器主线程
- 🎨 **彩色输出** - 使用 Minecraft 颜色代码美化所有输出信息
- 🔐 **权限控制** - 细粒度的权限节点控制命令使用
- 📝 **详细日志** - 完整的日志记录，便于调试和审计

## 🎯 使用指南

### 命令

插件提供了一个主命令 `/blackbe`，包含以下子命令：

#### 按玩家名查询
```bash
/blackbe name <玩家名>
```
查询指定玩家名是否在 BlackBE 数据库中。

**示例：**
```bash
/blackbe name Steve
```

#### 按 QQ 号查询
```bash
/blackbe qq <QQ号>
```
查询指定 QQ 号关联的玩家是否在 BlackBE 数据库中。

**示例：**
```bash
/blackbe qq 123456789
```

### 查询结果说明

查询成功后，插件会显示以下信息：

- ✅ **未找到记录** - 玩家未在黑名单中
- ⚠️ **找到记录** - 玩家在黑名单中，显示详细信息：
  - 玩家名称
  - XUID
  - 封禁原因
  - 风险等级
  - 关联 QQ 号

### 🔐 权限节点

| 权限节点 | 说明 | 默认 |
|---------|------|------|
| `blackbe.command` | 使用 `/blackbe` 命令的基础权限 | OP |
| `blackbe.command.name` | 使用 `/blackbe name` 子命令 | OP |
| `blackbe.command.qq` | 使用 `/blackbe qq` 子命令 | OP |

### API 端点

本插件使用 BlackBE 官方 API v3：
```
https://api.blackbe.work/openapi/v3/check
```

## 🐛 故障排除

**Q: 插件加载失败**
- 确保使用 Java 21 或更高版本
- 检查 Allay 版本是否兼容 (0.18.0+)

**Q: 查询总是失败**
- 检查服务器网络连接
- 确认 BlackBE API 服务可访问
- 查看日志了解详细错误信息

**Q: 玩家被错误拦截**
- 检查日志中的黑名单信息
- 确认玩家确实在 BlackBE 数据库中
- 如有误判，请联系 BlackBE 官方申诉

## 🔨 开发

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件