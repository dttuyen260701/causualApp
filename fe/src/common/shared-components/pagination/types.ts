export interface IPagination {
	totalCount: number;
	pageSize: number;
	siblingCount: number;
	currentPage: number;
	onPageChange: (page: number) => void;
}
