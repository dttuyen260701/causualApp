import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';

import { RootState } from '../../store';

import { IExpandSidebar } from './type';

export const initialState: IExpandSidebar = {
  isExpand: true
};
export const expandSidebarSlice = createSlice({
  name: 'expandSidebar',
  initialState,
  reducers: {
    setExpandSidebar: (state: IExpandSidebar, action: PayloadAction<boolean>) => {
      state.isExpand = action.payload;
    }
  }
});
export const { setExpandSidebar } = expandSidebarSlice.actions;
export const getCurrentExpandSidebar = (state: RootState): boolean => state.expandSidebar.isExpand;
export default expandSidebarSlice.reducer;
