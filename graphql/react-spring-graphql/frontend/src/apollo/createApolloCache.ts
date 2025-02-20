import { InMemoryCache } from '@apollo/client';
import { CursorPagedResponse } from '../generated/graphql';

export const createApolloCache = (): InMemoryCache => {
    return new InMemoryCache({
        typePolicies: {
            Query: {
                fields: {
                    getFilms: {
                        // 캐시가 키값으로 사용하는 인자 목록
                        // 기존 요청의 경우, limit 과 cursor 가 keyArgs 에 해당
                        // 페이지 처리 작업을 위해 특정 필드로 따로 캐시되어 저장 될 필요가 없어서 false
                        keyArgs: false,
                        // merge 함수는 새롭게 응답된 데이터와 기존 데이터를 어떻게 병합 처리할 것인지 명시
                        merge: (
                            existing: CursorPagedResponse | undefined,
                            incoming: CursorPagedResponse,
                        ): CursorPagedResponse => {
                            return {
                                cursor: incoming.cursor,
                                data: existing ? [...existing.data, ...incoming.data] : incoming.data,
                            };
                        },
                    }
                }
            }
        }
    });
};