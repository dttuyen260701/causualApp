import { ReactElement } from 'react';

export interface IItemSidebar {
  name: string
  icon: ReactElement
  navigate: string
  showAll?: boolean
  handleClickItem?: () => void
}
