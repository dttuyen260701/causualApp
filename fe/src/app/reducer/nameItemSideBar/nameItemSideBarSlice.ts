import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import { RootState } from '../../store';

import { INameItemSideBar } from './nameItemSideBarTypes';

export const initialState: INameItemSideBar = {
  name: 'Projects'
};

export const nameItemSideBarSlice = createSlice({
  name: 'nameItemSideBar',
  initialState,
  reducers: {
    setNameItemSideBar: (state: INameItemSideBar, action: PayloadAction<string>) => {
      state.name = action.payload;
    }
  }
});

export const { setNameItemSideBar } = nameItemSideBarSlice.actions;
export const getCurrentNameItemSideBar = (state: RootState): string => state.nameItemSideBar.name;
export default nameItemSideBarSlice.reducer;
