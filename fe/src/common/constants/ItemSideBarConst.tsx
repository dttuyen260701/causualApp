import React from "react";
import { AiOutlineHome } from "react-icons/ai";
import { MdOutlineWorkOutline } from "react-icons/md";

import { IItemSidebar } from "../shared-components/Layout/sidebar/sidebarTypes";

export const ItemSideBarList: IItemSidebar[] = [
	{ name: "Trang chủ", icon: <AiOutlineHome />, navigate: "/job-manage/home" },
	{ name: "Quản lý công việc", icon: <MdOutlineWorkOutline />, navigate: "/job-manage/job" }
];
